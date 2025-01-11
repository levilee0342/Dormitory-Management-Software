import { Card, CardBody } from "@nextui-org/card";
import { Room, RoomStatus } from "../../types";
import RoomDetailModal from "./RoomDetailModal";
import { useDisclosure } from "@nextui-org/react";
import { STATUS_MAP } from "../../utils/map";
import { priceFormat } from "../../utils/priceFormat";
export default function RoomCard({
  onRoomClicked,
  room,
  isStaff,
}: {
  onRoomClicked?: (room: Room) => void;
  isStaff: boolean;
  room: Room;
}) {
  const {
    isOpen: isDetailModalOpen,
    onOpen: onOpenDetailModal,
    onClose: onDetailModalClose,
  } = useDisclosure();

  const getBackground = () => {
    if (room.booking_count === 0 && room.status === "AVAILABLE") {
      return "bg-white";
    } else if (
      room.booking_count &&
      room.booking_count > 0 &&
      room.booking_count < room.type.capacity &&
      room.status === "AVAILABLE"
    ) {
      return "bg-yellow-50";
    } else if (
      room.booking_count === room.type.capacity ||
      room.status === RoomStatus.MAINTAINING
    ) {
      return "bg-red-100";
    }
  };
  return (
    <>
      <RoomDetailModal
        room={room}
        isStaff={isStaff}
        isOpen={isDetailModalOpen}
        onClose={onDetailModalClose}
      />
      <Card
        isPressable={isStaff}
        onPress={() => {
          onRoomClicked ? onRoomClicked(room) : onOpenDetailModal();
        }}
        shadow="none"
        radius="sm"
        className={`h-36 w-40 ${getBackground()} border-2`}
      >
        <CardBody className="items-center justify-center">
          <div className="font-semibold text-sm pb-3">{room.id}</div>
          <div className="w-full">
            <div className="flex items-center justify-between text-xs">
              <div>Đơn giá</div>
              <div className="text-right">{priceFormat(room.type.price)}</div>
            </div>
            <div className="flex items-center justify-between text-xs">
              <div>Số giường</div>
              <div className="text-right">{room.type.capacity}</div>
            </div>
            <div className="flex items-center justify-between text-xs">
              <div>Đang thuê</div>
              <div className="text-right">{room.booking_count ?? 0}</div>
            </div>
            <div className="flex items-center justify-between text-xs">
              <div>Trạng thái</div>
              <div className="text-right">{STATUS_MAP[room.status]}</div>
            </div>
          </div>
        </CardBody>
      </Card>
    </>
  );
}
